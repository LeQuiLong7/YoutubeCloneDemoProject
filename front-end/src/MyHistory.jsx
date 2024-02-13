
import { useState, useEffect } from "react";
import axios from "axios";
import VideoDetail from "./VideoDetail";
export default function  MyHistory({isHistory}) {
    const [videos, setVideos] = useState(null);
    useEffect(() => {
        fetchVideos();
    }, [isHistory]);
    async function fetchVideos() {
        var url = 'http://localhost:8080/users';
        if(isHistory) {
            url += '/history'
        } else {
            url += '/likedVideos'
        }
        const response = await axios.get(url, {
            withCredentials: true
        });

        const data = response.data

        if (JSON.stringify(data) === JSON.stringify(['empty'])) {

        } else {
            const videosPromise = data.map(id => (fetchVideosDetail(id)));
            var videos = await Promise.all(videosPromise);
            videos = videos.filter(v => v != '');
            setVideos(videos);

            // console.log(videos)
        }
    }
    async function fetchVideosDetail(videoId) {
        const response = await axios.get(`http://localhost:8080/videos/details/${videoId}`, {
            withCredentials: true
        });
        return response.data;
    }
    return (
        <>
            <div >
                <div className='your-channel-nav'>
                    <span className='nav-video'>Video</span>
                    <span className='nav-title'>Title</span>
                    <span className='nav-views'>Views</span>

                </div>
                {
                    videos != null ? (

                        videos.map(video => (
                            <VideoDetail deletable={false} fetchVideos={fetchVideos} key={Math.random()} id={video.id} thumnail={video.thumbnailURL} title={video.title} views={video.totalViews} />
                        ))
                    ) : (<p className='empty'>Empty!</p>)
                }

            </div>
        </>)

}