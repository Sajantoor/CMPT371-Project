import './App.css';
import Board from './components/Board';
import Cursor from './components/Cursor';


function App() {
  return (
    <div className="App">
      <div className="container">
        <h1> Deny and Conquer </h1>
        <Board />
        <Cursor />
      </div>
    </div>
  );
}

export default App;
