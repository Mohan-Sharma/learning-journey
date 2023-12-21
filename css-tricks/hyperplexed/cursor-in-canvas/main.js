
const canvas = document.querySelector('.canvas');
window.addEventListener('mousemove', (e) => {
    const mouseX = e.clientX, mouseY = e.clientY;

    const mouseXPercent = mouseX / window.innerWidth;
    const mouseYPercent = mouseY / window.innerHeight;

    // canvas is large so substracting it from inner window(i.e. screen) we get the extra area (extraOffsetArea) it can move
    // so when screen point is 0, 0 canvas area should be 0 * extraOffsetArea
    // offset** is always the position of the element including the padding, margin etc
    // client** is the visible area of the screen from the border of the screen to the point of click
    // page** is the area of the page to the point of click, in case of overflow page width or height is more and hidden from screen area. In this case
    // client** and page** will differ
    const maxX = canvas.offsetWidth - window.innerWidth; // element width from start - browser window width will give -ve means move left
    const maxY = canvas.offsetHeight - window.innerHeight;

    const moveX = maxX * mouseXPercent * -1; // reverse the cursor:area movement
    const moveY = maxY * mouseYPercent * -1;

    canvas.animate(
            { transform: `translate(${moveX}px, ${moveY}px)`},
            {duration: 4000, fill: 'forwards', easing: 'ease'});
});