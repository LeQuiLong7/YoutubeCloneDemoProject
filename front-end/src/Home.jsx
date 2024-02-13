
import { useEffect, useState } from 'react';
import axios from 'axios';
import Video from './Video';
import { useNavigate } from 'react-router-dom';
export default function Home() {

  const [userId, setUserId] = useState('');
  const navigate = useNavigate();
  // const [avtUrl, setAvtUrl] = useState('');
  const [videos, setVideos] = useState([]);

  useEffect(() => {
    checkLoggedIn();
    fetchUserInfo();
    fetchVideos();
  }, []);

  async function checkLoggedIn() {
    const response = await axios.get('http://localhost:8080/hello', {
      withCredentials: true
    });
    if (response.status != 200) {
      navigate('/');
    }

  }
  async function fetchUserInfo() {
    const response = await axios.get('http://localhost:8080/info', {
      withCredentials: true
    });
    const data = response.data
    setUserId(data.principal.attributes.name)
    localStorage.setItem("avt-url", data.principal.attributes.picture)
  }

  async function fetchVideos() {
    const response = await axios.get('http://localhost:8080/videos/1', {
      withCredentials: true
    });

    const data = response.data
    // console.log(data)
    setVideos(data)
  }
  // async function fetchOn


  return (
    <>
      {
        videos.map(video => (
          <Video key={video.videoId} id={video.videoId} thumnail={video.thumbnailURL} channelName={video.chanelName} title={video.title} views={video.totalViews} />
        ))
      }
    </>
  )

}