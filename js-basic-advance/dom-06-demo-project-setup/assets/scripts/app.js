const addMovie = document.getElementById('add-movie');
const modal = document.getElementById('add-modal');
const add = document.querySelector('.btn--success');
const cancel = document.querySelector('.btn--passive');
const backdrop = document.getElementById('backdrop');
const inputs = document.querySelectorAll('input')
const deleteMovieModal = document.getElementById('delete-modal');
const movies = [];


const toggleMovieModal = () => {
    modal.classList.toggle('visible');
    toggleModalBackdrop();
};

const cancelMovieModal = () => {
    toggleMovieModal();
    clearInputs();
};

const toggleModalBackdrop = () => {
    backdrop.classList.toggle('visible');
};

const clearInputs = () => {
    for (let input of inputs) {
        input.value = '';
    }
};

const deleteMovie = (movieId) => {
    closeDeleteMovieModal();
    const list  = document.getElementById(movieId);
    list.remove();
};

const closeDeleteMovieModal = () => {
    alert('hi')
    toggleModalBackdrop();
    deleteMovieModal.classList.remove('visible');
}

const deleteMovieHandler = (movieId) => {
    deleteMovieModal.classList.add('visible');
    toggleModalBackdrop();
    const removeMovieBtn = document.querySelector('.btn--danger');
    const cancelBtn = document.querySelector('.btn--danger');
    cancelBtn.addEventListener('click', () => {
        toggleModalBackdrop();
        deleteMovieModal.classList.remove('visible');
    });
    removeMovieBtn.addEventListener('click', deleteMovie.bind(null, movieId));
};

const insertMovieList = (id, title, imageURL, rating) => {
    const movieLi = document.createElement('li');
    movieLi.classList.add('movie-element');
    movieLi.id = id;
    movieLi.innerHTML = `
        <div class="movie-element__image">
            <img src="${imageURL}" alt="${title}">
        </div>
        <div class="movie-element__info">
            <h2>${title}</h2>
            <p>${rating}/5 stars</p>
        </div>
    `
    const movieListElement = document.getElementById('movie-list');
    movieLi.addEventListener('click', deleteMovieHandler.bind(null, id));
    movieListElement.appendChild(movieLi);
};

const addMovieToList = () => {
    const title = inputs[0].value.trim();
    const imageURL = inputs[1].value.trim();
    const rating = +inputs[2].value.trim();
    if (title === '' || imageURL === '' || rating === '' || rating < 1 || rating > 5) {
        alert('Enter valid input!');
        return;
    }
    const movie = {
        id: Math.random().toString(),
        movieTitle : title,
        imageURL: imageURL,
        rating : rating
    }
    movies.push(movie);
    toggleMovieModal();
    insertMovieList(movie.id, movie.movieTitle, movie.imageURL, movie.rating);
    clearInputs();
}

addMovie.addEventListener('click', toggleMovieModal)
add.addEventListener('click', addMovieToList)
cancel.addEventListener('click', cancelMovieModal)
