import Block from "./Block";

export default function Board(props) {
    let board = [];
    let size = 4;

    for (let i = 0; i < size; i++) {
        let row = [];
        for (let j = 0; j < size; j++) {
            row.push(<Block key={j + i * size} />);
        }
        board.push(<div className="row" key={i}>{row}</div>);
    }

    return (
        <div className="board">
            {board}
        </div>
    );
}