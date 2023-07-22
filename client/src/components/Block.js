import { useEffect, useState } from "react";


export default function Block(props) {
    const [color, setColor] = useState("gray");

    useEffect(() => {
        console.log("Block rendered");
    }, []);

    function handleClick() {
        setColor(color === "gray" ? "blue" : "gray");
    }

    return (
        <div className="block" onClick={handleClick} style={{ backgroundColor: color }}>
        </div>
    );
}

