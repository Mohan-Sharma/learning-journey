import './style.css'

const parallax_elems = document.querySelectorAll('.parallax');

let moveX, moveY = 0;
window.addEventListener('mousemove', (e) => {
    moveX = e.clientX - window.innerWidth / 2;
    moveY = e.clientY - window.innerHeight / 2;

    parallax_elems.forEach(elem => {
        const speedX = elem.dataset.speedX;
        const speedY = elem.dataset.speedY;
        elem.style.translate = `calc(-50% + ${-moveX * speedX}px) calc(-50% + ${-moveY * speedY}px)`
    });
});