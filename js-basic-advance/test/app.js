// Get the canvas element and its context
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");

// Set the initial position and size of the boy
var x = 50;
var y = 200;
var width = 50;
var height = 100;

// Draw the boy
function drawBoy() {
    // Clear the canvas
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Draw the boy
    ctx.fillStyle = "#f00";
    ctx.fillRect(x, y, width, height);
}

// Move the boy to the right
function moveRight() {
    // Increment the x position
    x += 10;

    // Redraw the boy
    drawBoy();
}

// Move the boy to the left
function moveLeft() {
    // Decrement the x position
    x -= 10;

    // Redraw the boy
    drawBoy();
}

// Add event listeners for the arrow keys
document.addEventListener("keydown", function(event) {
     console.log(event.keyCode);
    if (event.keyCode === 39) {
        moveRight();
    } else if (event.keyCode === 37) {
        moveLeft();
    }
});

// Draw the boy initially
drawBoy();
