import React, { useState } from 'react';
import axios from 'axios';

function App() {
  const [modelName, setModelName] = useState('Product');
  const [fields, setFields] = useState([{name:'id', type:'Long'}, {name:'name', type:'String'}]);
  const [businessLogic, setBusinessLogic] = useState('If price > 1000 apply 10% discount');
  const [response, setResponse] = useState('');
  const [dynamicResp, setDynamicResp] = useState('');

  const updateField = (index, key, val) => {
    const copy = [...fields];
    copy[index][key] = val;
    setFields(copy);
  };

  const addField = () => setFields([...fields, {name:'', type:'String'}]);
  const removeField = (i) => setFields(fields.filter((_,idx)=>idx!==i));

  const generate = async () => {
    try {
      const res = await axios.post('/api/generate', { modelName, fields, businessLogic, operations: ['CREATE'] });
      setResponse(JSON.stringify(res.data || res, null, 2));
    } catch (e) {
      setResponse(e.toString());
    }
  };

  const callDynamic = async () => {
    try {
      const res = await axios.post(`/api/${modelName.toLowerCase()}`, { test: true, sample: 123 });
      setDynamicResp(JSON.stringify(res.data, null, 2));
    } catch (e) {
      setDynamicResp(e.toString());
    }
  };

  return (
    <div style={{padding:20, fontFamily:'sans-serif'}}>
      <h2>Auto API Creator - UI</h2>
      <div>
        <label>Model Name: </label>
        <input value={modelName} onChange={e=>setModelName(e.target.value)} />
      </div>
      <div style={{marginTop:10}}>
        <label>Fields:</label>
        {fields.map((f,i)=>(
          <div key={i} style={{display:'flex', gap:10, marginTop:6}}>
            <input value={f.name} onChange={e=>updateField(i,'name',e.target.value)} placeholder="name" />
            <input value={f.type} onChange={e=>updateField(i,'type',e.target.value)} placeholder="type" />
            <button onClick={()=>removeField(i)}>Remove</button>
          </div>
        ))}
        <button onClick={addField} style={{marginTop:8}}>Add Field</button>
      </div>

      <div style={{marginTop:10}}>
        <label>Business Logic (plain english):</label><br/>
        <textarea value={businessLogic} onChange={e=>setBusinessLogic(e.target.value)} rows={4} cols={60} />
      </div>

      <div style={{marginTop:10}}>
        <button onClick={generate}>Generate API</button>
        <button onClick={callDynamic} style={{marginLeft:10}}>Call Dynamic Endpoint</button>
      </div>

      <div style={{marginTop:20}}>
        <h3>Generate response</h3>
        <pre>{response}</pre>
      </div>

      <div style={{marginTop:20}}>
        <h3>Dynamic endpoint response</h3>
        <pre>{dynamicResp}</pre>
      </div>
    </div>
  );
}

export default App;
