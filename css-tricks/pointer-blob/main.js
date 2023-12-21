const blob = document.querySelector(".blob");

document.body.addEventListener('pointermove', (e) => {
    moveBlob(e);
});

const moveBlob = (e) => {
    const {clientX, clientY } = e;

    blob.animate({
        left:  `${clientX}px`,
        top: `${clientY}px`
    }, { duration: 3000, fill: "forwards" })
};