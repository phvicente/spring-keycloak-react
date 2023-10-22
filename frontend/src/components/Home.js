import React, { useEffect, useState } from 'react'
import {  loanApi } from './LoansListApi'
import { Layout, Col, Row } from 'antd'
import { useAuth } from "react-oidc-context"
import AuthBar from './AuthBar'
import { Typography } from 'antd'
import LoanForm from "./LoanForm";
import LoansTable from "./LoansTable";


const { Header, Content } = Layout
const { Title } = Typography

function Home() {
    const [loans, setLoans] = useState([])
    const auth = useAuth()
    const access_token = auth.user.access_token


    useEffect(() => {
        handleLoans()
    }, [])
    const handleLoans = async () => {
        try {
            const response =  await loanApi.getLoansByCustomerId(access_token)
            setLoans(response.data)
        } catch (error) {
            handleLogError(error)
        }
    }
    const onFinish = async (loanRequest) => {
        try {
            const { loanValue, numberInstallments } = loanRequest;
            const createLoanRequest = { loanValue: parseFloat(loanValue), numberInstallments: parseInt(numberInstallments) };
             // Substitua pelo seu token de acesso
            await loanApi.createLoan(createLoanRequest, access_token)
            await handleLoans()

        } catch (error) {
            handleLogError(error)
        }
    }
    const isUser = () => {
        const { profile } = auth.user
        return profile
    }

    const handleLogError = (error) => {
        if (error.response) {
            console.log(error.response.data)
        } else if (error.request) {
            console.log(error.request)
        } else {
            console.log(error.message)
        }
    }
    const headerStyle = {
        textAlign: 'center',
        color: '#fff',
        backgroundColor: '#333',
        fontSize: '3em'
    }
    return (
        <Layout>
            <Header style={headerStyle}>Loans</Header>
            <AuthBar />
            {isUser() ? (
                <Content>
                    <Row justify="space-evenly">
                        <Col span={6}>
                            <LoanForm onFinish={onFinish}
                            />
                        </Col>
                        <Col span={17}>
                            <LoansTable
                                loans={loans}
                                />
                        </Col>
                    </Row>
                </Content>
            ) : (
                <div style={{ textAlign: "center" }}>
                    <Title>Oops ...</Title>
                    <Title level={2} style={{ color: 'grey' }}>It looks like you do not have the LOAN role!</Title>
                </div>
            )}
        </Layout>
    )
}
export default Home
