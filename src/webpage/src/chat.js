import React, { useState, useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const Chat = () => {
    const [client, setClient] = useState(null);
    const [connected, setConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [username, setUsername] = useState('');
    const [usernameSet, setUsernameSet] = useState(false);
    const [newMessage, setNewMessage] = useState("");
    const [newUsername, setNewUsername] = useState("");

    const messageSend = "/snd/sendMessage";
    const messageReceive = "/rcv/messages";
    const registration = "/snd/register";
    const ping = "/user/rcv/ping";
    const pong = "/snd/pong";
    const nameSet = "/snd/naming";
    const nameGet = "/user/rcv/namingResponse";

    useEffect(() => {
        if ("Notification" in window) {
            Notification.requestPermission().then(r => console.log(r));
        }

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

        const stompClient = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log("Connected!");

                setConnected(true);

                stompClient.subscribe(messageReceive, message => {
                    const newMsg = JSON.parse(message.body);
                    setMessages(prevMessages => [newMsg, ...prevMessages]);

                    if (newMsg.text.includes(`@${username}`) && document.visibilityState === "hidden") {
                        if (Notification.permission === "granted") {
                            new Notification(`You were mentioned by ${newMsg.username}`, {
                                body: newMsg.text
                            });
                        }
                    }
                });

                stompClient.subscribe(ping, () => {
                    console.log("Ping received");
                    console.log("Sending pong");
                    stompClient.publish({
                        destination: pong
                    });
                });

                stompClient.subscribe(nameGet, message => {
                    let response = JSON.parse(message.body);
                    if (response.response === "1"){
                        setUsername(response.name);
                        setUsernameSet(true);
                        console.log("Username set to: " + response.name);
                    } else {
                        alert("Username already taken!");
                    }
                });

                stompClient.publish({
                    destination: registration
                });
            },
            onWebSocketClose: () => {
                console.log("Web Socket Close.");

                setConnected(false);
            },
            onDisconnect: () => {
                console.log("Disconnected!");

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
            reconnectDelay: 2500,
        });

        stompClient.activate();
        setClient(stompClient);

        // Disconnect the STOMP client when the component unmounts
        return () => {
            stompClient.deactivate().then(r => console.log(r));
        };
    }, [username]);

    const sendMessage = () => {
        if (connected && newMessage) {
            let message = {
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

    const assignUsername = () => {
        if (connected && newUsername) {
            let request = {
                name: newUsername
            }

            console.log("Sending username:", request);

            client.publish({
                destination: nameSet,
                body: JSON.stringify(request),
            });
            setNewUsername("");
        }
    }

    return (
        <div>
            {!usernameSet ? (
                <>
                    <div>Set Username</div>
                    <input
                        type="text"
                        placeholder="Enter username..."
                        value={newUsername}
                        onChange={(e) => setNewUsername(e.target.value)}
                        onKeyUp={(e) => {
                            if (e.key === "Enter") {
                                assignUsername();
                            }
                        }}
                    />
                    <button onClick={assignUsername} disabled={!connected}>Set</button>
                </>
            ) : (
                <>
                    <input
                        type="text"
                        placeholder="Type a message..."
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        onKeyUp={(e) => {
                            if (e.key === "Enter") {
                                sendMessage();
                            }
                        }}
                    />
                    <button onClick={sendMessage} disabled={!connected}>Send</button>
                    <div className="message-container">
                        {messages.map((msg, idx) => {
                            const isMentioned = msg.text.includes(`@${username}`);
                            const messageClass = `message ${isMentioned ? 'message-mentioned' : ''}`;

                            return (
                                <div key={idx} className={messageClass}>
                                    {msg.username} ({new Date(msg.timestamp).toLocaleTimeString()}): {msg.text}
                                </div>
                            );
                        })}
                    </div>
                </>
            )}
        </div>

    );

};

export default Chat;
