package com.company.courseManager.question.domain;

public enum EnumQuestionStatus {
	//1：待支付 2：新问题，3：待回复 4：已回复 ，5问题失效 6：问题结束

	Zero,
	waitPay,
	NewQuestion,
	WaitAnswer,
	HavedAnswered,
	Expired,
	Closed
}
