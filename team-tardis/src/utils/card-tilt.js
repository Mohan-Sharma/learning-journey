import VanillaTilt from "vanilla-tilt";

export const tiltCard = () => {
    const tiltingCards = document.querySelectorAll('.tilting__card');
    const tiltOptions= { reverse: true, max: 30, transition:true };
    VanillaTilt.init(tiltingCards, tiltOptions);
};