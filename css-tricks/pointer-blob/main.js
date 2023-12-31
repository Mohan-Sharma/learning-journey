const blob = document.querySelector(".blob");

document.body.addEventListener('mousemove', (e) => {
    moveBlob(e);
});

const moveBlob = (e) => {
    const clientX = e.clientX - blob.offsetWidth/2;
    const clientY = e.clientY - blob.offsetWidth/2;
    const keyframes = {
        left: `${clientX}px`,
        top: `${clientY}px`
    };

    blob.animate(keyframes, { duration: 300, fill: "forwards" })
};