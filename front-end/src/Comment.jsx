import { useEffect, useState } from "react";
import axios from "axios";
export default function Comment({ userId, comment }) {

    const [userInfo, setUserInfo] = useState(null)
    useEffect(() => {
        fetchChanelName();
    }, []);

    async function fetchChanelName() {
        const response = await axios.get(`http://localhost:8080/users/info/${userId}`, {
            withCredentials: true
        });

        const data = response.data;
        // console.log(data)
        setUserInfo(data)
    }
    return (
        <>

            <div>

                {
                    userInfo === null ? (<p>Loading...</p>) : (<p> <span className="bold"> {userInfo.chanelName}</span>: {comment}</p>)
                }
            </div>


        </>
    )
}