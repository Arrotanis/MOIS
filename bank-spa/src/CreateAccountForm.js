/*

import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import axios from 'axios';

const CreateAccountForm = () => {
    const [ownerAccountId, setOwnerAccountId] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('http://localhost:8080/api/transaction/create-account', {
                ownerAccountId: ownerAccountId
            });
            if (response.ok) {
                const message = response.data;
                alert(message);
            } else {
                throw new Error('Failed to create account');
            }
        } catch (error) {
            console.error('Error creating account:', error);
            alert('Failed to create account');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <TextField
                fullWidth
                label="Owner Account ID"
                type="text"
                value={ownerAccountId}
                onChange={(e) => setOwnerAccountId(e.target.value)}
                variant="outlined"
                sx={{ marginBottom: '1rem' }}
            />
            <Button type="submit" variant="contained" color="primary">
                Create Account
            </Button>
        </form>
    );
};

export default CreateAccountForm;

 */
