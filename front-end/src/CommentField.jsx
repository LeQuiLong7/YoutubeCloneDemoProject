import { useState } from "react";
import axios from "axios";

export default function CommentField({ videoId, setVideoDetails }) {

    const [comment, setComment] = useState('');

    async function postComment() {

        const formData = new FormData();

        formData.append('comment', comment);

        const response = await axios.post(`http://localhost:8080/videos/comments/${videoId}`, formData, {
            withCredentials: true
        });

        const data = response.data
        setVideoDetails((prevData) => ({
            ...prevData,
            comments: [...prevData.comments, data],
        }));

        setComment('');
    }
    return (
        <div className='comment'>
            <input type="text" className="commentInput" value={comment} onChange={e => setComment(e.target.value)} />
            <button onClick={postComment}>Post</button>
        </div>
    )

}