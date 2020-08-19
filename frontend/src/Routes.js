import React from "react";
import {Route, Switch} from "react-router-dom";

import Register from "./pages/register/Register";
import Login from "./pages/login/Login";
import Home from "./pages/home/Home";
import Add from "./pages/add/Add";
import Read from "./pages/read/Read";
import Decks from "./pages/decks/Decks";
import Deck from "./pages/decks/EditDeck";

export default () => (
    <Switch>
      <Route exact path="/" component={() => <Home/>}/>
      <Route path="/add" component={() => <Add/>}/>
      <Route path="/deck/:id" component={Deck}/>
      <Route path="/decks" component={() => <Decks/>}/>
      <Route path="/read/:id" component={Read}/>
      <Route path="/read" component={Read}/>
      <Route path="/register" component={() => <Register/>}/>
      <Route path="/login" component={() => <Login/>}/>

    {/*  TODO default 404 */}
    </Switch>
)