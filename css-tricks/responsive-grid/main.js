const tilesWrapper = document.querySelector(".tiles");

let rows = 0,
        cols = 0,
        toggled = false;

const toggle = () => {
    toggled = !toggled;
    document.body.classList.toggle("toggled");
}

const handleTileClicked = (index) => {
    toggle();

    anime({
        targets: ".tile",
        opacity: toggled ? 0 : 1,
        delay: anime.stagger(50, {
            grid: [cols, rows],
            from: index
        })
    });
}

const createTile = index  => {
    const tile = document.createElement('div');
    tile.classList.add('tile');
    tile.addEventListener('click',  handleTileClicked.bind(null, index));
    return tile;
}

const createTiles = numberOfTiles => {
    Array.from(Array(numberOfTiles).keys()).forEach( i => {
        tilesWrapper.appendChild(createTile(i));
    });
};

const createGrid = () => {
    tilesWrapper.innerHTML = "";

    const tileSize = document.body.clientWidth > 800 ? 100 : 50;
    rows = Math.floor(document.body.clientHeight / tileSize);
    cols = Math.floor(document.body.clientWidth / tileSize);

    tilesWrapper.style.setProperty("--rows", rows);
    tilesWrapper.style.setProperty("--cols", cols);
    createTiles(rows * cols);
}

createGrid();
window.onresize = () => createGrid();
