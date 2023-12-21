const loveBtn = document.querySelector('.love-btn');

const hateBtn = document.querySelector('.hate-btn');

const cardGroups = document.querySelectorAll('.cards__wrapper');

let activeIndex =  0;
const swipeRight = (e) => {
    console.log(e.target.classList);
    let nextIndex = activeIndex + 1 < cardGroups.length ? activeIndex + 1 : 0;

    const currentCardGroup = document.querySelector(`[data-index="${activeIndex}"]`);
    const nextCardGroup = document.querySelector(`[data-index="${nextIndex}"]`);

    currentCardGroup.dataset.status = "after";
    nextCardGroup.dataset.status = "pending-active-left";
    setTimeout(() => {
        nextCardGroup.dataset.status = "active";
        activeIndex = nextIndex;
    });
};

const swipeLeft = (e) => {
    console.log(e.target.classList);
    let nextIndex = activeIndex + 1 < cardGroups.length ? activeIndex + 1 : 0;

    const currentCardGroup = document.querySelector(`[data-index="${activeIndex}"]`);
    const nextCardGroup = document.querySelector(`[data-index="${nextIndex}"]`);

    currentCardGroup.dataset.status = "before";
    nextCardGroup.dataset.status = "pending-active-right";
    setTimeout(() => {
        nextCardGroup.dataset.status = "active";
        activeIndex = nextIndex;
    });
};

loveBtn.addEventListener('click', swipeRight);
hateBtn.addEventListener('click', swipeLeft);