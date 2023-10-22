import React from 'react'
import { Typography, Form, Input, Button } from 'antd'
const { Title } = Typography
function LoanForm({ onFinish: onFinish }) {
    return (
        <>
            <Title level={3}>Create Loan</Title>
            <Form
                name="basic"
                autoComplete="off"
                onFinish={onFinish}
            >
                <Form.Item
                    label="Loan Value"
                    name="loanValue"
                    rules={[{ required: true, message: 'Please enter the loan value!' }]}
                >
                    <Input type="number" />
                </Form.Item>
                <Form.Item
                    label="Number of Installments"
                    name="numberInstallments"
                    rules={[{ required: true, message: 'Please enter the number of installments!' }]}
                >
                    <Input type="number" />
                </Form.Item>
                <Form.Item>
                    <Button
                        type="primary"
                        shape="round"
                        htmlType="submit"
                    >
                        Submit
                    </Button>
                </Form.Item>
            </Form>
        </>
    )
}

export default LoanForm;
