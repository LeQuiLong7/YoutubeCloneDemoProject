import { useNavigate } from "react-router-dom"
import axios from "axios";
import { useState, useEffect } from "react";
import SubscribedChannel from "./SubscribedChanel";

export default function Nav() {

    const [userInfo, setUserInfo] = useState(null);

    useEffect(() => {
        fetchUserInfo();
      }, []);

    async function fetchUserInfo() {
        const response = await axios.get('http://localhost:8080/users', {
          withCredentials: true
        });
        const data = response.data
        setUserInfo(data);
      }
    

    const navigate = useNavigate();
    async function home() {
        navigate("/home")
    }

    async function yourChannel() {
        navigate("/your-channel")
    }
    async function yourChannel() {
        navigate("/your-channel")
    }

    return <div className="nav">
        <p onClick={home}>Home</p>
        <hr/>
        <p onClick={yourChannel}>Your channel</p>
        <p onClick={e => navigate('/history')}>History</p>
        <p>Your videos</p>
        <p onClick={e => navigate('/liked')}>Liked videos</p>
        <hr></hr>
        <p>Subscriptions</p>
        {userInfo != null && 
            userInfo.subscribedToIds.map(id => (
                <SubscribedChannel key={Math.random()} channelId={id} />
            ))
        }
    </div>

}