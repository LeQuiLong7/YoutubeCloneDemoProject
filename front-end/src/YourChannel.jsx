
import { useEffect, useState } from 'react';
import axios from 'axios';
import Video from './Video';
import VideoDetail from './VideoDetail';
import { useNavigate } from 'react-router-dom';
import Upload from './Upload';
export default function YourChannel() {

    const [videos, setVideos] = useState(null);
    const [uploadState, setUploadState] = useState(false);

    useEffect(() => {
        fetchVideos();
    }, []);



    async function fetchVideos() {
        const response = await axios.get('http://localhost:8080/users/videos/all', {
            withCredentials: true
        });

        const data = response.data

        if (JSON.stringify(data) === JSON.stringify(['empty'])) {

        } else {
            const videosPromise = data.map(id => (fetchVideosDetail(id)));
            const videos = await Promise.all(videosPromise);
            setVideos(videos);
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
                    <span className='nav-actions'>Actions</span>

                </div>
                {
                    videos != null ? (

                        videos.map(video => (
                            <VideoDetail deletable={true} fetchVideos={fetchVideos} key={video.id} id={video.id} thumnail={video.thumbnailURL} title={video.title} views={video.totalViews} />
                        ))
                    ) : (<p className='empty'>Empty!</p>)
                }
                {
                    uploadState == true &&   (
                        <Upload setUploadState={setUploadState} fetchVideos={fetchVideos}></Upload>
                    )
                }
                <button className='upload' onClick={e => setUploadState(true)}>Upload</button>
            </div>


        </>
    )

}