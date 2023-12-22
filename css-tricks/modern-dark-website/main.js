const bubbles = document.querySelector('.bubbles');
const between = (min, max) => (Math.random() *  (max - min)) + min;
const colors = [`hsl(193, 100%, 68%)`, `hsl(0 0% 100% / .7)`, `hsla(324, 76%, 68%, 0.8)`]

const createBubble = () => {
    const bubble = document.createElement('div');
    const bubbleSize = `${between(4, 8)}px`;
    const floatingKeyFrames = [{ top: '100%'}, { top: `-${bubbleSize}`}]
    const randomColor = colors[Math.floor(Math.random() * colors.length)];
    bubble.style.width = bubbleSize;
    bubble.style.left= `${between(0, 100)}%`;
    bubble.style.background= randomColor;
    bubble.style.opacity = `${between(0.1, 1)}`;
    bubble.classList.add('bubble');
    bubbles.appendChild(bubble);
    const panOutBubble = bubble.animate(floatingKeyFrames, { duration: between(10000, 40000), fill: 'forwards'});

    panOutBubble.onfinish = () => {
        console.log("removing", bubble);
        bubbles.removeChild(bubble);
    };
};

setInterval(createBubble, 500);