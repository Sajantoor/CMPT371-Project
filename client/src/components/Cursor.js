import { useEffect, useState } from "react";

export default function Cursor(props) {
    const [x, setX] = useState(0);
    const [y, setY] = useState(0);

    function handleMouseMove(e) {
        setX(e.clientX);
        setY(e.clientY);
    }

    useEffect(() => {
        window.addEventListener("mousemove", handleMouseMove);
        return () => {
            window.removeEventListener("mousemove", handleMouseMove);
        }
    }, []);


    return (
        <div className="cursor" style={{ top: y - 15, left: x - 15 }}>
        </div>
    );
}