import { useEffect, useState } from 'react';
import avatar from './static/xp.jpg'
import axios from 'axios';
export default function Channel({ videoId }) {
    const [userInfo, setUserInfo] = useState(null);
    const [channelInfo, setChannelInfo] = useState(null);
    useEffect(() => {
        fetchChannelInfo();
        fetchUserInfo();
    }, []);

    async function fetchChannelInfo() {
        const response = await axios.get(`http://localhost:8080/users/detail-by-videoId/${videoId}`, {
            withCredentials: true
        });

        const data = response.data;
        setChannelInfo(data);
    }

    async function like() {
        // const response = await axios.put(`http://localhost:8080/users/likeVideo?videoId=${videoId}`, {
        //     withCredentials: true
        // });
        const response = await axios.put(`http://localhost:8080/users/likeVideo?videoId=${videoId}`,{}, {
            withCredentials: true
        });
        if (response.status == 200) {
            fetchUserInfo();
        }
    }

    async function subscribe() {
        const data = {
            subscribeToId: channelInfo.id,
            subscriberId: userInfo.id
        }
        const response = await axios.post(`http://localhost:8080/users/subscribe`, data, {
            withCredentials: true
        });
        console.log(response)
        if (response.status == 201) {
            fetchUserInfo();
            fetchChannelInfo();
        }
    }

    async function fetchUserInfo() {
        const response = await axios.get(`http://localhost:8080/users`, {
            withCredentials: true
        });

        const data = response.data;
        setUserInfo(data);
    }


    return <div className="channel">
        <div className='channel-info'>
            <img src={avatar} className='avt' alt="" />
            {
                channelInfo != null ?
                    (<div className='inline'>
                        <p className='bold'>{channelInfo.chanelName}</p>
                        <p>{channelInfo.subscribersIds.length} subscribers</p>
                    </div>) :
                    (<p>Loading</p>)
            }
            {
                (userInfo != null && channelInfo != null) ? userInfo.subscribedToIds.includes(channelInfo.id) ?
                    (
                        <button className='subscribed'>Subscribed</button>
                    ) : (
                        <button className='unsubscribed' onClick={subscribe}>Subscribe</button>
                    ) : (
                    <p>Loading</p>
                )
            }
        </div>
        <div className='actions'>
            {
                (userInfo != null && channelInfo != null) ? userInfo.likedHistory.includes(videoId) ?
                    (
                        <button className='subscribed'>Liked</button>
                    ) : (
                        <button className='unsubscribed' onClick={like}>Like</button>
                    ) : (
                    <p>Loading</p>
                )
            }
            {/* <button>Like</button> */}
            <button>Share</button>
            <button>Download</button>
        </div>
    </div>
}