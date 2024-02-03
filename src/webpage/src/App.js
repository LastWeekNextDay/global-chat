import React from 'react';
import Chat from './chat.js';
import './App.css';

function App() {
    return (
        <div className="App">
            <h4>@LastWeekNextDay</h4>
            <header className="App-header">
                <h1>React WebSocket Chat</h1>
            </header>
            <Chat />
        </div>
    );
}

export default App;
