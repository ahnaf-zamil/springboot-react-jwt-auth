import axios from "axios";
import React, { useState } from "react";
import { AuthResponse } from "../types";
import { API_URL, endpoints } from "../config.json";

export const RegisterPage: React.FC = () => {
  const [name, setName] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const handleSubmit = async (e: React.SyntheticEvent) => {
    e.preventDefault();
    const resp: AuthResponse = (
      await axios.post(API_URL + endpoints.REGISTER, { name, email, password })
    ).data;
    localStorage.setItem("refreshToken", resp.refreshToken);
    window.location.href = "/";
  };

  return (
    <div className="h-screen flex items-center">
      <div className="mx-auto container lg:w-2/5 md:3/5">
        <h1 className="font-bold text-2xl text-center">Create an Account</h1>
        <div className="bg-white rounded px-8 pt-6 pb-8 mb-4 flex flex-col">
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="block text-grey-darker text-sm font-bold mb-2">
                Name
              </label>
              <input
                className="border focus:outline-none focus:ring focus:border-blue-300 rounded w-full py-2 px-3 text-grey-darker"
                id="name"
                name="name"
                type="text"
                placeholder="John Doe"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            <div className="mb-4">
              <label className="block text-grey-darker text-sm font-bold mb-2">
                Email
              </label>
              <input
                className="border focus:outline-none focus:ring focus:border-blue-300 rounded w-full py-2 px-3 text-grey-darker"
                id="email"
                name="email"
                type="email"
                placeholder="johndoe@gmail.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="mb-6">
              <label className="block text-grey-darker text-sm font-bold mb-2">
                Password
              </label>
              <input
                className="border focus:outline-none focus:ring focus:border-blue-300 rounded w-full py-2 px-3 text-grey-darker mb-3"
                id="password"
                type="password"
                placeholder="VeryStrongPassword@123"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <h1>
                Already have an account?{" "}
                <a href="/login" className="text-blue-500 hover:text-blue-700">
                  Log in!
                </a>
              </h1>
            </div>
            <div className="flex items-center justify-center">
              <button
                className="bg-blue-500 hover:bg-blue-dark text-white font-bold py-2 px-4 rounded"
                type="submit"
              >
                Sign Up
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};
