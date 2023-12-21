const title = document.querySelector('.title');
const alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
title.addEventListener('mouseover', (e) => {
    randomizeWords(e);
});

const randomizeWords = (e) => {

    let itrCount = 0;
    const interval = setInterval(() => {

        e.target.innerHTML =
                e.target.innerHTML
                .split("")
                .map((letter, index) => {
                    if (itrCount > index) {
                        return e.target.dataset.value[index];
                    }
                    return alphabet[Math.floor(Math.random() * 26)];
                })
                .join("");

        if (itrCount > e.target.dataset.value.length) {
            clearInterval(interval);
        }

        itrCount += 1/3;

    }, 30);
};