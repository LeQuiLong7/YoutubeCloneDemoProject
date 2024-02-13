
// import thumnail from "./static/xp.jpg"

import axios from 'axios';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function VideoDetail({ id, thumnail, title, views, fetchVideos, deletable }) {

    const [youSure, setYouSure] = useState(false)
    const navigate = useNavigate();

    function watch() {
        const url = `/watch?id=${id}`
        navigate(url);
    }
    async function deleteVideo() {
        const response = await axios.delete(`http://localhost:8080/users/${id}`, {
            withCredentials: true
        });
        fetchVideos();
    }

    return (
        <>

            <div className="detail" >
                <div>
                    <img onClick={watch} className="thumnail-detail" src={thumnail} alt="" />

                </div>
                <div>
                    <h2>{title}</h2>
                </div>
                <div>
                    <h4>{views} views</h4>
                </div>
                {deletable && (
                    <div>
                        <button className='delete' onClick={e => { setYouSure(true) }}>DELETE</button>
                    </div>
                )}
            </div>
            {
                youSure &&
                <div className="you-sure">
                    <h3>You sure?</h3>
                    <div>
                        <button onClick={e => { e.preventDefault(); deleteVideo() }}>Yes</button>
                        <button onClick={e => { e.preventDefault(); setYouSure(false) }}>Cancel</button>
                    </div>
                </div>
            }

        </>

    )
}