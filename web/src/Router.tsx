import React from "react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import { LandingPage } from "./pages/LandingPage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { Wrapper } from "./Wrapper";

export const Router: React.FC = () => {
  return (
    <BrowserRouter>
        <Switch>
          <Route exact path="/" component={() => <Wrapper><LandingPage/></Wrapper>} />
          <Route exact path="/login" component={LoginPage} />
          <Route exact path="/register" component={RegisterPage} />
        </Switch>
    </BrowserRouter>
  );
};

