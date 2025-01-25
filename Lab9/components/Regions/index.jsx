import React from "react";
import "./styles.css";

class Regions extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      substr: "",
      filteredRegions: [],
    };

    this.handleChangeSubstr = this.handleChangeSubstr.bind(this);
  }

  handleChangeSubstr(event) {
    const substr = event.target.value.toLowerCase();
    const regionsModel = window.regionsModel.regionsModel();
    const filteredRegions = regionsModel.filter(region =>
      region.toLowerCase().includes(substr)
    );

    this.setState({
      substr: substr,
      filteredRegions: filteredRegions,
    });
  }

  render() {
    const { substr, filteredRegions } = this.state;

    return (
      <div>
        <div className="state-search">
          {substr}
        </div>

        <div className="lab9-example-output">
          {filteredRegions.length > 0 ? (
            <ul>
              {filteredRegions.map((region, index) => (
                <li key={index}>{region}</li>
              ))}
            </ul>
          ) : (
            <p>No matching regions found.</p>
          )}
        </div>

        <label htmlFor="substrId">Enter substring to search:</label>
        <input
          id="substrId"
          type="text"
          value={substr}
          onChange={this.handleChangeSubstr}
        />
      </div>
    );
  }
}

export default Regions;