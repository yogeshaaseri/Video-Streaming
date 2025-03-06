import { useState } from 'react'
import './App.css'
import VideoUpload from "./components/VideoUpload"
import toast, { Toaster } from 'react-hot-toast';
import { Button, TextInput } from 'flowbite-react';
function App() {
  const [count, setCount] = useState(0)

  const [fieldValue,setFieldValue] = useState(null)

  const [videoId, setVideoId] = useState(
    "4f7e6efc-7df9-4570-9245-176718f7329b"
  );
  // const [selectedFile, selectedFile] = useState(null);
  function handlefileChange(event) {
    console.log(event);
  }

  function playVideo(videoId){
    setVideoId(videoId);
  }

  return (
    <>
      <Toaster />

      <div className='flex flex-col items-center space-y-6 justify-center py-9'>
        <h1 className='text-3xl font-bold text-gray-700 dark:text-gray-100'>Welcome to video Streaming Application</h1>

        <div className='flex justify-around w-full '>
          <div className=''>
            <h1 className="text-gray-950 dark:text-white text-center mt-2 ">Video player</h1>

            <video className='w-full' src={`http://localhost:8080/api/v1/videos/stream/range/${videoId}`} style={{
              width: 500,
            }} controls ></video>
          </div>

          <VideoUpload />
        </div>
        <div className='my-4 flex space-x-4'>
                <TextInput onClick={(event) => {
                  setFieldValue(event.target.value);
                }} placeholder='Enter Video Id' name='video_Id_Field' />
                <Button onClick={()=> {
                  setVideoId(fieldValue);
                }} >Play</Button>
        </div>
      </div>

             
    </>
  );
}

export default App
