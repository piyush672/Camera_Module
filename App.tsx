import React, { useEffect, useState } from 'react'
import { Button, Image, PermissionsAndroid, View } from 'react-native'
import { NativeModules } from "react-native";

const { CameraModule, CustomGalleryPermission } = NativeModules


function App() {
  const [imageUri, setImageUri] = useState("");
  async function getPermission() {
    await PermissionsAndroid.requestMultiple(
      [PermissionsAndroid.PERMISSIONS.CAMERA, PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE],
    );
  }

  async function takePicture() {
    const granted = await CustomGalleryPermission.requestStoragePermission()

    if (granted) {
      CameraModule.onClickCamera()
        .then((uri: string) => {
          console.log("uri is", uri)
          setImageUri(uri)
        })
        .catch((error: any) => console.log("error is", error))
    }
  }

  async function pickFromGallery() {
    const granted = await CustomGalleryPermission.requestStoragePermission()

    if (granted) {
      CameraModule.onPickFromGallery()
        .then((uri: string) => {
          console.log("uri is", uri)
          setImageUri(uri)
        })
        .catch((error: any) => console.log("error is", error))
    }
  }

  useEffect(() => {
    try {
      getPermission()
    } catch (err) {
      console.warn(err);
    }
  }, [])

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Button title='Click photo' onPress={takePicture} />
      <Button title='Pick from gallery' onPress={pickFromGallery} />
      {imageUri && (
        <Image
          source={{ uri: imageUri }}
          style={{ width: 300, height: 300, resizeMode: 'contain' }}
        />
      )}
    </View>
  )
}

export default App