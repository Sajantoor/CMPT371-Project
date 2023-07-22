import { useEffect, useState } from "react";


const defaultColor = "gray";
const nextColor = "blue";
const timeToSet = 1000; // takes 1 second to change color 

export default function Block(props) {
    const [color, setColor] = useState(defaultColor);
    const [lastTime, setLastTime] = useState(0);
    const [transition, setTransition] = useState(false);

    useEffect(() => {
    }, []);


    function handleMouseDown(event) {
        if (event.button !== 0) return; // only left click

        // set lastTime to current time 
        setLastTime(Date.now());
        setTransition(true);
        setColor(nextColor);
    }

    function handleMouseUp(event) {
        const currentTime = Date.now();
        const timeElapsed = currentTime - lastTime;
        console.log(timeElapsed);


        if (timeElapsed < timeToSet) {
            setColor(defaultColor);
        }

        setTransition(false);
    }

    return (
        <div
            className={transition ? "block transition" : "block"}
            onMouseDown={handleMouseDown}
            onMouseUp={handleMouseUp}
            style={{ backgroundColor: color }}
        >

        </div>
    );
}

