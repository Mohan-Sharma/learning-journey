const blob = document.querySelector(".blob");

document.body.addEventListener('mousemove', (e) => {
    const img = e.target.closest(".interactable__img"),
            intersectingImg = img !== null;
    moveBlob(e, intersectingImg);
});

const moveBlob = (e, intersecting) => {
    const clientX = e.clientX - blob.offsetWidth/2;
    const clientY = e.clientY - blob.offsetWidth/2;
    const keyframes = {
        left: `${clientX}px`,
        top: `${clientY}px`,
        scale: `${intersecting ? 2.5 : 1}`
    };

    blob.animate(keyframes, { duration: 300, fill: "forwards" })
};