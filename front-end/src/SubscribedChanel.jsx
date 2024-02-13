import axios from "axios";
import { useState, useEffect } from "react";
import avatar from './static/xp.jpg'

export default function SubscribedChannel({channelId}) {

    const [chanelInfo, setChanelInfo] = useState(null);

    useEffect(() => {   
        fetchChannelInfo();
      }, []);
    async function fetchChannelInfo() {
        const response = await axios.get(`http://localhost:8080/users/full-info-by-id/${channelId}`, {
            withCredentials: true
          });
      
        const data = response.data
        setChanelInfo(data);
    }
    return (<div className='subscribed-channel'>
        <img src={avatar} alt="alt" />
        {chanelInfo != null ? (<p>{chanelInfo.chanelName}</p>) : (<p>Loading</p>)}
    </div>)
}