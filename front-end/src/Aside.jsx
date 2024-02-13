import { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
export default function Aside({ videoId, thumbnailURL, title, chanelName, totalViews, setKey }) {

    const navigate = useNavigate();


     function nav() {
        
        // console.log("hello")
        //  navigate(`/home`);
         const url = `/watch?id=${videoId}`
         navigate(url);
        setKey(pre => pre + 1);
    }
    return (
        <>
            <div className="aside" key={videoId} onClick={nav}>
                <div>
                    <img src={thumbnailURL} className="aside-img" alt="" />
                </div>
                <div className="text">
                    <h2>{title}</h2>
                    <h3> {chanelName}</h3>
                    <h4>{totalViews} views</h4>
                </div>
            </div>

        </>
    )




}