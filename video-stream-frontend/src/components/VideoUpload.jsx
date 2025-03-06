import React, { useState } from "react"
import videologo from "../assets/upload.png";
import { Alert, Button, Card, FileInput, Label, Progress, Textarea, TextInput } from "flowbite-react";
import axios from 'axios';
import toast from "react-hot-toast";

function VideoUpload() {

    const [selectedFile, setSelectedFile] = useState(null);
    const [meta, setMeta] = useState({
        title: "",
        description: "",
    });
    const [progress, setProgress] = useState(0);
    const [uploading, setUploading] = useState(false);
    const [message, setMessage] = useState("");

    function handlefileChange(event) {
        // console.log(event.target.files[0]);
        setSelectedFile(event.target.files[0]);
    }

    function formFieldChange(event){
        // console.log(event.target.name);
        // console.log(event.target.value);
        setMeta({
            ...meta,
        [event.target.name]:event.target.value
        });        
    }
    

    function handleform(formEvent) {
        formEvent.preventDefault();
        // console.log(formEvent.target.title.value);
        // console.log(formEvent.target.description.value);
        console.log(meta);
        if(!selectedFile){
         alert("select video file")
          return;
        }
        // submit file to server
        saveVideoToServer(selectedFile,meta);

    }

    function resetForm(){
        setMeta({
            title:"",
            description:""
        });
        setSelectedFile(null);
        setUploading(false)

    }

    async function saveVideoToServer(video,videoMetaData) {
        setUploading(true);

        // api calls
        try {

            let formData = new FormData;
            formData.append("title",videoMetaData.title);
            formData.append("description",videoMetaData.description);
            formData.append("file",selectedFile)

          let response = await axios.post(`http://localhost:8080/api/v1/videos`,formData,{
                headers: {
                    "Content-Type":"multipart/form-data",
                },
            onUploadProgress: (progressEvent) => {
                const progress=Math.round((progressEvent.loaded * 100) / progressEvent.total);

                console.log("thsi is prgressing" + progress);
                setProgress(progress);
            },
            });

            console.log(response);
            console.log(response.data.videoId +"  uploaded video id")
            setMessage( response.data.videoId);
            setUploading(false);
            toast.success("File uploading");
            resetForm();
        } catch (error) {
            console.log(error);
            setMessage("error in uploading file");
            setUploading(false);
            toast.error("file not uploaded");

        }


    }

    return <div className="text-white">
        <Card className="flex flex-col items-center justify-center w-fit">
            <h1>Video upload</h1>
            <div>
                <form noValidate className="flex flex-col space-y-6" onSubmit={handleform}>

                    <div>
                        <div className="mb-2 block">
                            <Label htmlFor="file-upload" value="Video title" />
                        </div>
                        <TextInput onChange={formFieldChange} value={meta.title} name="title" placeholder="Enter Title" />
                    </div>

                    <div className="max-w-md">
                        <div className="mb-2 block">
                            <Label htmlFor="comment" value="Video Description" />
                        </div>
                        <Textarea onChange={formFieldChange} name="description" id="comment" placeholder="Write Video Description" value={meta.description} required rows={4} />
                    </div>

                    <div className="flex items-center space-x-5 justify-center">
                        <div className="shrink-0">
                            <img className="h-16 w-16 object-cover " src={videologo} alt="Current profile photo" />
                        </div>
                        <label className="block">
                            <span className="sr-only">Choose profile photo</span>
                            <input onChange={handlefileChange} name="file" type="file" className="block w-full text-sm text-slate-500
      file:mr-4 file:py-2 file:px-4
      file:rounded-full file:border-0
      file:text-sm file:font-semibold
      file:bg-violet-50 file:text-violet-700
      hover:file:bg-violet-100
    "/>
                        </label>
                    </div>
                       
                         <div>
                        <Progress 
                          progress={progress} progressLabelPosition="inside" textLabel="Uploading" textLabelPosition="outside"  size="xl" labelProgress  labelText />
                         </div>

                        <div>
                        {message &&  <Alert color="success" onDismiss={() => alert('Alert dismissed!')}>
      <span className="font-medium">suc </span> {message}
    </Alert>}
                        </div>

                    <div className="flex justify-center">
                        <Button type="submit">Submit</Button>
                    </div>
                </form>
            </div>

        </Card>
    </div>
}

export default VideoUpload