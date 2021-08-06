import axios from "axios";
import React, { useEffect, useState } from "react";
import { setAccessToken } from "./tokenManager";
import { AuthResponse } from "./types";
import {API_URL, endpoints} from "./config.json";

export const Wrapper: React.FC = (props) => {
    const [hasToken, setHasToken] = useState<boolean>(false);

    if (!localStorage.getItem("refreshToken")) {
        window.location.href = "/login"
    }

    useEffect(() => {
        axios.post(API_URL + endpoints.REFRESH_TOKEN, {
            refreshToken: localStorage.getItem("refreshToken")
        }).then(resp => resp.data).then((data: AuthResponse) => {
            localStorage.setItem("refreshToken", data.refreshToken);
            setAccessToken(data.accessToken);
            setHasToken(true);
        }).catch(err => window.location.href = "/login")
    }, [])

    if (hasToken) {
        return <div>{props.children}</div>;
    }
    else {
        return <div>Loading...</div>
    }
}