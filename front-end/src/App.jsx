import { Routes, Route, BrowserRouter as Router } from "react-router-dom";
import Home from "./Home";
import Login from "./Login";
import './style.css';
import Watch from "./Watch";
import Upload from "./Upload";
import Search from "./Search";
import { useState } from "react";
import Nav from "./Nav";
import YourChannel from "./YourChannel";
import MyHistory from "./MyHistory";


export default function App() {

  return (<>
    <Routes>
      <Route path={"/"} exact element={<Login />}></Route>
      <Route path="/home" element={
        <>
          <Search />
          <div className="content">
            <Nav></Nav>
            <div className="videos">
              <Home />
            </div>
          </div>
        </>
      }>

      </Route>
      <Route path="/watch" element={
        <>
          <Search />
          <div className="content">
            <Nav></Nav>
              <Watch />
          </div></>}
      >

      </Route>
      <Route path="/upload" element={
        <>
          <Search />
          <div className="content">
            <Nav></Nav>
            <div className="videos">
              <Upload />
            </div>
          </div></>}>

      </Route>
      <Route path="/your-channel" element={
        <>
          <Search />
          <div className="content">
            <Nav></Nav>
          </div>
          <YourChannel />
        </>}>
      </Route>
      <Route path="/history" element={
        <>
          <Search />
          <div className="content">
            <Nav></Nav>
          </div>
          <MyHistory isHistory={true}/>
        </>}>
      </Route>
      <Route path="/liked" element={
        <>
          <Search />
          <div className="content">
            <Nav></Nav>
          </div>
          <MyHistory isHistory={false} />
        </>}>
      </Route>
    </Routes>
  </>
  )
}

