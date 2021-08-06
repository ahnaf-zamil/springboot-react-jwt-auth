import { getAccessToken, setAccessToken } from "../tokenManager";
import React, { useState, useEffect } from "react";
import { User } from "../types";
import axios from "axios";
import { API_URL, endpoints } from "../config.json";

export const LandingPage: React.FC = () => {
  const token = getAccessToken();

  const [user, setUser] = useState<User | null>(null);

    const logoutUser = () => {
        localStorage.removeItem("refreshToken");
        setAccessToken("");
        window.location.href = "/"
    }

  useEffect(() => {
    axios
      .get(API_URL + endpoints.GET_CURRENT_USER, {
        headers: {
          Authorization: token,
        },
      })
      .then((resp) => resp.data)
      .then((data: User) => {
        setUser(data);
      });
  }, []);

  if (!user) {
    return <div>Loading...</div>;
  } else {
    return (
      <div className="h-screen flex items-center">
        <div className="container mx-auto">
          <h1 className="font-bold text-3xl text-center">
            Logged in as, {user.name}
          </h1>
          <div className="flex justify-center mt-5">
            <div>
              <h1>
                <span className="font-bold">ID:</span> {user.id}
              </h1>
              <h1>
                <span className="font-bold">Email:</span> {user.email}
              </h1>
              <div className="flex items-center justify-center">
                <button
                  className="bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded mt-5"
                  onClick={(e) => logoutUser()}
                >
                  Logout
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
};
