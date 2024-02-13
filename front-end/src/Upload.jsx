import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
export default function Upload({ setUploadState, fetchVideos }) {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [selectedImage, setSelectedImage] = useState(null);
    const [selectedVideo, setSelectedVideo] = useState(null);
    const [uploading, setUpLoading] = useState(false);

    // const navigate = useNavigate()


    async function create() {
        var formData = new FormData();
        formData.append('title', title);
        formData.append('description', description);
        formData.append('thumbnailFile', selectedImage);
        formData.append('videoFile', selectedVideo);
        setUpLoading(true);
        const response = await axios.post('http://localhost:8080/videos', formData, {
            withCredentials: true
        });
        setUpLoading(false)
        if (response.status == 200) {
            setUploadState(false);
            // navigate('/your-channel')
            fetchVideos()
        }
    }
    return (
        <>
            <div className="create-form">
                {uploading ? (<p>Uploading...</p>) : (
                    <form>
                        <div>
                            <label htmlFor="mv-name">Title: </label>
                            <input type="text" value={title} id="mv-name" onChange={e => setTitle(e.target.value)} />
                        </div>
                        <div>
                            <label htmlFor="mv-name">Description: </label>
                            <input type="text" value={description} id="mv-name" onChange={e => setDescription(e.target.value)} />
                        </div>
                        <div>
                            <label htmlFor="mv-img">Thumbnail: </label>
                            <input type="file" id="mv-img" onChange={e => setSelectedImage(e.target.files[0])} />
                        </div>
                        <div>
                            <label htmlFor="mv-video">Video file: </label>
                            <input type="file" id="mv-video" onChange={e => setSelectedVideo(e.target.files[0])} />
                        </div>
                        <div className="mv-img-display">
                            {selectedImage && <img src={URL.createObjectURL(selectedImage)} alt="Selected Image" />}
                        </div>
                        <div>
                            <button className="create-bt" onClick={e => { e.preventDefault(); create() }}>Upload</button>
                        </div>
                    </form>
                )}
                <button className="cancel" onClick={e => setUploadState(false)} >X</button>
            </div>
        </>


    )
}