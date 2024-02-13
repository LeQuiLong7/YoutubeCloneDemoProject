import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function WatchVideo({videoId}) {

    const [videoDetail, setVideoDetails] = useState(null);

    useEffect(() => {
        fetchVideo();
    },[]);


    async function fetchVideo() {
        const view = await axios.get(`http://localhost:8080/users/view/${videoId}`, {
            withCredentials: true
        });
        const videoResponse = await axios.get(`http://localhost:8080/videos/details/${videoId}`, {
            withCredentials: true
        });
        const video = videoResponse.data
        setVideoDetails(video)

    }

    return (
        <>
            {videoDetail == null ? (<p>Loading...</p>) : <>
                <div id="video-container">
                    <video controls poster={videoDetail.thumbnailURL} >
                        <source src={videoDetail.videoURL} type="video/mp4" />
                    </video>
                </div>
                <h3>{videoDetail.title}</h3>
                <h4>{videoDetail.totalViews} views</h4>
            </>}
        </>
    )
}