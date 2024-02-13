
// import thumnail from "./static/xp.jpg"

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Video({id, thumnail, title, channelName, views}) {


    const navigate = useNavigate();

    function watch() {
        const url = `/watch?id=${id}`
        navigate(url);
    }


    return (
        <div className="video" onClick={watch}> 
            <img className="thumnail" src={thumnail} alt="" />
            <h2>{title}</h2>
            <h3>{channelName}</h3>
            <h4>{views} views</h4>
        </div>
    )
}