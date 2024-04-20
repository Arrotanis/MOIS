import React, { useState } from 'react';

function CreateAccountForm() {
    const [ownerAccountId, setOwnerAccountId] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        const response = await fetch('http://localhost:8080/api/transaction/create-account', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // Correctly indicates that the body is JSON
            },
            body: JSON.stringify({ ownerAccountId: ownerAccountId }) // Correctly format the body as JSON
        });

        if (response.ok) {
            const message = await response.text();
            alert(message);
        } else {
            alert('Failed to create account');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                Owner Account ID:
                <input
                    type="text"
                    value={ownerAccountId}
                    onChange={(e) => setOwnerAccountId(e.target.value)}
                />
            </label>
            <button type="submit">Create Account</button>
        </form>
    );
}

export default CreateAccountForm;
