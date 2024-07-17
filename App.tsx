import React, { useEffect, useState } from 'react'
import { Button, Image, PermissionsAndroid, View } from 'react-native'
import { NativeModules } from "react-native";

const { CameraModule } = NativeModules


function App() {
  const [imageUri, setImageUri] = useState("");
  async function getPermission() {
    const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.CAMERA,
      {
        title: 'Cool Photo App Camera Permission',
        message:
          'Cool Photo App needs access to your camera ' +
          'so you can take awesome pictures.',
        buttonNeutral: 'Ask Me Later',
        buttonNegative: 'Cancel',
        buttonPositive: 'OK',
      },
    );
    if (granted === PermissionsAndroid.RESULTS.GRANTED) {
      console.log('You can use the camera');
    } else {
      console.log('Camera permission denied');
    }
  }

  async function takePicture() {
    const granted = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.CAMERA)

    if (granted) {
      CameraModule.onClickCamera()
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