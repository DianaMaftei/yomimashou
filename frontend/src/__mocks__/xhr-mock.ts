const xhrMockClass = () => ({
    open            : jest.fn().mockReturnValue("mock"),
    send            : jest.fn().mockReturnValue("mock"),
    onload            : jest.fn(),
    setRequestHeader: jest.fn(),
    responseText: "mock"
});

window.XMLHttpRequest = jest.fn().mockImplementation(xhrMockClass)