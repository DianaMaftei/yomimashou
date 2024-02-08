import React from 'react';
import { Route, Switch } from 'react-router-dom';
import Add from './pages/add/Add';

import Authenticate from './pages/authentication/Authenticate';
import Decks from './pages/decks/Decks';
import Deck from './pages/decks/EditDeck';
import Home from './pages/home/Home';
import Practice from './pages/practice/Practice';
import Read from './pages/read/Read';


const Routes = () => (
    <Switch>
      <Route exact path="/" component={() => <Home/>}/>
      <Route path="/practice/:id" component={() => <Practice/>}/>
      <Route path="/add" component={() => <Add/>}/>
      <Route path="/deck/:id" component={Deck}/>
      <Route path="/decks" component={() => <Decks/>}/>
      <Route path="/read/:id" component={Read}/>
      <Route path="/read" component={Read}/>
      <Route path="/register" component={() => <Authenticate/>}/>
      <Route path="/login" component={() => <Authenticate/>}/>

    {/*  TODO default 404 */}
    </Switch>
)

export default Routes;
