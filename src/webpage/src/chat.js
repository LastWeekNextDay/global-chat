import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const Chat = () => {
    const [client, setClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [username, setUsername] = useState('');
    const [clientBrowser, setClientBrowser] = useState("");
    const [newMessage, setNewMessage] = useState("");
    const [messageLog] = useState("/chat/log");
    const[messageSend] = useState("/chat/sendMessage");
    const[messageReceive] = useState("/messages");

    useEffect(() => {
        let enteredUsername = prompt("Please enter your username:", "Guest");
        if (!enteredUsername || enteredUsername.trim() === '') {
            enteredUsername = 'Guest'; // Default username if none entered
        }
        setUsername(enteredUsername);
        let clientBr = navigator.userAgent;
        setClientBrowser(clientBr);

        const socket = new SockJS('http://localhost:8080/chatApp');
        socket.onopen = () => {
            console.log("Socket Open.");
        }

        socket.onclose = () => {
            console.log("Socket Close.");
        }

        socket.onerror = (event) => {
            console.log("Socket Error");
            console.error(event);
        }

        socket.onmessage = (event) => {
            console.log("Socket Message");
            console.log(event);
        }

        // Connect to the WebSocket server
        const stompClient = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log("Connected!");
                let log = {
                    message: enteredUsername + " (" + clientBr + ") connected.",
                    timestamp: Date.now(),
                }
                stompClient.publish({
                    destination: messageLog,
                    body: JSON.stringify(log),
                    skipContentLengthHeader: true
                })
                setConnected(true);
                stompClient.subscribe(messageReceive, message => {
                    setMessages(prevMessages => [...prevMessages, JSON.parse(message.body)]);
                });
            },
            onWebSocketClose: () => {
                console.log("Web Socket Close.");
                setConnected(false);
            },
            onDisconnect: () => {
                console.log("Disconnected!");
                let log = {
                    message: enteredUsername + " (" + clientBr + ") disconnected.",
                    timestamp: Date.now(),
                }
                stompClient.publish({
                    destination: messageLog,
                    body: JSON.stringify(log),
                    skipContentLengthHeader: true
                })
                setConnected(false);
            },
            onStompError: (frame) => {
                console.log("STOMP ERROR");
                console.error(frame);
                setConnected(false);
            },
            onWebSocketError: (event) => {
                console.log("Web Socket Error");
                console.error(event);
                setConnected(false);
            },
            reconnectDelay: 1000,
        });

        stompClient.activate();
        setClient(stompClient);

        // Disconnect the STOMP client when the component unmounts
        return () => {
            stompClient.deactivate().then(r => console.log(r));
        };
    }, []);

    // Handler for sending messages
    const sendMessage = () => {
        if (connected && newMessage) {
            let user = {
                username: username,
                client: clientBrowser,
            }
            let message = {
                source: user,
                text: newMessage,
                timestamp: Date.now(),
            };
            console.log("Sending message:", message);
            client.publish({
                destination: messageSend,
                body: JSON.stringify(message),
                skipContentLengthHeader: true
            });
            setNewMessage("");
        }
    };

    return (
        <div>
            <div>
                {messages.map((msg, idx) => (
                    <div key={idx}>{msg.text}</div>
                ))}
            </div>
            <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                onKeyUp={(e) => {
                    if (e.key === "Enter") {
                        sendMessage();
                    }
                }}
            />
            <button onClick={sendMessage} disabled={!connected}>Send</button> {/* Disable button when not connected */}
        </div>
    );
};

export default Chat;
