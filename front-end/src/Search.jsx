import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
export default function Search() {

    const [email, setEmail] = useState('')
    const [fullname, setFullName] = useState('')
    const [channelName, setChannelName] = useState('')
    const [showInfo, setShowInfo] = useState(false)

    const navigate = useNavigate();
    useEffect(() => {
        fetchUserInfo();
    }, []);

    async function fetchUserInfo() {
        const response = await axios.get('http://localhost:8080/info', {
            withCredentials: true
        });

        const response2 = await axios.get('http://localhost:8080/users', {
            withCredentials: true
        });

        const data = response.data
        const data2 = response2.data
        // console.log(data);
        // console.log(data2)
        setEmail(data.principal.email)
        setFullName(data.principal.fullName)
        setChannelName(data2.chanelName)
    }

    return (
        <>
            <div className="search">
                <div>
                    <button className="home-btn" onClick={e => navigate('/home')}>LongTube</button>
                </div>
                <div>
                    <input type="text" className="search-bar" />
                    <button>Search</button>
                </div>
                <img className="avt" onClick={e => setShowInfo(pre => !pre)} src={localStorage.getItem('avt-url')}></img>
            </div>
            {showInfo && (
                <div className="search-info-menu">
                    <img className="avt menu" src={localStorage.getItem('avt-url')}></img>
                    <p className="email first">{fullname}</p>
                    <p className="email">@{email}</p>
                    <p className="bold left">Your channel: {channelName}</p>
                </div>
            )}
        </>
    )
}