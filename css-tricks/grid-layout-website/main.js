const leftArrows = document.querySelectorAll('.go__left');
const rightArrows = document.querySelectorAll('.go__right');

const projects = document.querySelectorAll('.projects');

let activeIndex =  0;

const swipeRight = (e) => {
    swipe(e, 'after', 'pending-active-left');
};

const swipeLeft = (e) => {
    swipe(e, 'before', 'pending-active-right');
};

const swipe = (e, currentProjectStatus, nextProjectStatus) => {
    let nextIndex = activeIndex + 1 < projects.length ? activeIndex + 1 : 0;
    const currentProject = document.querySelector(`[data-index="${activeIndex}"]`);
    const nextProject = document.querySelector(`[data-index="${nextIndex}"]`);
    currentProject.dataset.status = currentProjectStatus;
    nextProject.dataset.status = nextProjectStatus;

    setTimeout(() => {
        nextProject.dataset.status = 'active';
        activeIndex = nextIndex;
    });
};
rightArrows.forEach(rightArrow => {
    rightArrow.addEventListener('click', swipeRight);
});

leftArrows.forEach(leftArrow => {
    leftArrow.addEventListener('click', swipeLeft);
});