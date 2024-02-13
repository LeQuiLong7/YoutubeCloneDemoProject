import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import Channel from './Channel';
import Comment from './Comment';
import Aside from './Aside';
import WatchVideo from './WatchVideo';
import CommentField from './CommentField';

export default function Watch() {

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const initialVideoId = queryParams.get('id');

    const [videoId, setVideoId] = useState(initialVideoId);


    const [key, setKey] = useState(0);
    const [asideVideos, setAsideVideos] = useState([]);
    const [videoDetail, setVideoDetails] = useState(null);
    // const [comment, setComment] = useState('');

    useEffect(() => {
        fetchVideo();
        fetchAsideVideos();
        setVideoId(queryParams.get('id'));
    }, [key]);

    async function fetchVideo() {
        const view = await axios.get(`http://localhost:8080/users/view/${videoId}`, {
            withCredentials: true
        });
        const videoResponse = await axios.get(`http://localhost:8080/videos/details/${videoId}`, {
            withCredentials: true
        });
        const video = videoResponse.data
        console.log("new video")
        setVideoDetails(video)

    }
    async function fetchAsideVideos() {
        var randomNumber = Math.floor(Math.random() * 10) + 1;
        const response = await axios.get(`http://localhost:8080/videos/${randomNumber}`, {
            withCredentials: true
        });

        const data = response.data;
        console.log(data)
        setAsideVideos(data)
    }


    // async function postComment() {

    //     const formData = new FormData();

    //     formData.append('comment', comment);

    //     const response = await axios.post(`http://localhost:8080/videos/comments/${videoId}`, formData, {
    //         withCredentials: true
    //     });

    //     const data = response.data
    //     setVideoDetails((prevData) => ({
    //         ...prevData,
    //         comments: [...prevData.comments, data],
    //     }));

    //     setComment('');
    // }
    return (
        <>
            {videoDetail === null ? (
                <p>Loading...</p>
            ) : (
                <div className='watch' >
                    <div className='video-container'>
                       <WatchVideo key={Math.random()} videoId={videoId} />
                        <Channel key={Math.random()}  videoId={videoId}/>

                        <h4 className='des'>{videoDetail.description}</h4>
                        <h3>Comments</h3>
                        {/* <div className='comment'>
                            <input type="text" className="commentInput" value={comment} onChange={e => setComment(e.target.value)} />
                            <button onClick={postComment}>Post</button>
                        </div> */}
                        <CommentField videoId={videoId} setVideoDetails={setVideoDetails}/>
                        {
                            videoDetail.comments.map(comment => (
                                <Comment key={Math.random()} userId={comment.userId} comment={comment.comment} />
                            ))
                        }
                    </div>
                    <div className="aside-container" key={Math.random()}>
                        {
                            asideVideos.map(video => (
                            <Aside setKey={setKey} key={Math.random()} videoId={video.videoId} thumbnailURL={video.thumbnailURL} chanelName={video.chanelName} title={video.title} totalViews={video.totalViews} />
                            ))
                        }
                    </div>
                </div>
            )} 
        </>
    )
}