let cards = document.querySelectorAll(".card");
let stackArea = document.querySelector(".stack-area");
let title = document.querySelector(".title");

function rotateCards() {
    let angle = 0;
    cards.forEach((card) => {
        if (card.classList.contains("active")) {
            card.style.transform = `translateX(-50vw) translateY(-10vw)`;
            title.style.transform= `translateY(-20vw)`;
        } else {
            card.style.transform = `translate(-50%, -50%) rotate(${angle}deg)`;
            angle = angle - 10;
        }
    });
}

rotateCards();

window.addEventListener("scroll", () => {
    let proportion =
            stackArea.getBoundingClientRect().top / window.innerHeight;
    if (proportion <= 0) {
        let n = cards.length;
        let index = Math.ceil(proportion / 0.33);
        index = Math.abs(index) - 1;
        for (let i = 0; i < n; i++) {
            if (i <= index) {
                cards[i].classList.add("active");
            } else {
                cards[i].classList.remove("active");
            }
        }
        rotateCards();
    }
});