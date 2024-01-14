import React, {useEffect, useState} from "react";
import "./style.css";
import SearchIcon from "./search.svg"
import Movie from "./movie";

const App = () => {

    const searchURL = 'http://www.omdbapi.com/?apikey=1c830453&s';
    const [title, setTitle] = useState('Batman');
    const [movies, setMovies] = useState([]);
    const searchMovies = async (title) => {
        const response = await fetch(`${searchURL}&s=${title}`)
        const data = await response.json();
        setMovies(data.Search);
        console.log(data.Search);
    };

    useEffect(() => {
        searchMovies('Batman');
    }, []);

    return (
           <div className="app">
               <h1 className="name">Movies</h1>
               <div className="search">
                   <input type="text" value={title} placeholder="Search movies" onChange={(e) => {setTitle(e.target.value)}} className="searchInput"/>
                   <img src={SearchIcon} alt="search movies icon" className="searchIcon" onClick={() => { searchMovies(title) }}/>
               </div>

               {movies.length > 0 ? (
                       <div className="container">
                           {movies.map((movie) => (<Movie movie={movie} />))}
                       </div>
                       )
                : (
                        <div className="empty">
                           <h2>No movies found</h2>
                        </div>)
               }
           </div>
    );
};

export default App;